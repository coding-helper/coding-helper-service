package org.pplm.gadgets.coder.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.pplm.gadgets.coder.entity.Base;
import org.pplm.gadgets.coder.entity.Dict;
import org.pplm.gadgets.coder.entity.Func;
import org.pplm.gadgets.coder.entity.Opt;
import org.pplm.gadgets.coder.entity.Project;
import org.pplm.gadgets.coder.repository.DictRepository;
import org.pplm.gadgets.coder.repository.FuncRepository;
import org.pplm.gadgets.coder.repository.OptRepository;
import org.pplm.gadgets.coder.repository.ProjectRepository;
import org.pplm.gadgets.coder.utils.ResHelper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@RestController
@RequestMapping(path = "/v1/gen", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeneratorController {

	// @Autowired
	private ProjectRepository projectRepository;

	// @Autowired
	private FuncRepository funcRepository;

	// @Autowired
	private OptRepository optRepository;

	// @Autowired
	private DictRepository dictRepository;

	@PostMapping(path = "/func/{framework}/{type}/{fid}")
	public Map<String, Object> onPostGenOpt(@PathVariable(name = "framework", required = true) String framework,
			@PathVariable(name = "type", required = true) String type,
			@PathVariable(name = "fid", required = true) Long fid) {

		return null;
	}

	@PostMapping(path = "/vue/{id}")
	public Map<String, Object> onPostList(@PathVariable(name = "id") String id,
			@RequestParam(name = "type", required = false, defaultValue = "list") String type)
			throws IOException, TemplateException {
		Func func = null; // funcRepository.findOne(id);
		if (func == null) {
			return ResHelper.error(ResHelper.MESSAGE_ERROR_ID);
		}
		return ResHelper.success(genCode(func, type + ".ftl", "/iview-admin"));
	}

	@PostMapping(path = "/vue/dict/{id}")
	public Map<String, Object> onPostDictGen(@PathVariable(name = "id") String id)
			throws IOException, TemplateException {
		Dict dict = null; // dictRepository.findOne(id);
		return ResHelper.success(genCode(dict, "dict.ftl", ""));
	}

	@PostMapping(path = "/vue/permission/{pid}")
	public Map<String, Object> onPermissionGen(@PathVariable(name = "pid") String pid)
			throws IOException, TemplateException {
		Project project = null; // projectRepository.findOne(pid);
		return ResHelper.success(genCode(project, "/wsh/iview-admin/permission.ftl", ""));
	}

	@PostMapping(path = "/vue/opt/{type}/{id}")
	public Map<String, Object> onPostSaveGen(@PathVariable(name = "type") String type,
			@PathVariable(name = "id") String id) throws IOException, TemplateException {
		Opt opt = null; // optRepository.findOne(id);
		String templateFileName = null;
		if ("update".equals(type) || "add".equals(type)) {
			templateFileName = "save-wsh.ftl";
		} else if ("save".equals(type)) {
			templateFileName = "/wsh/iview-admin/save-wsh.ftl";
		} else if ("detail".equals(type)) {
			templateFileName = "/wsh/iview-admin/detail-wsh.ftl";
		} else {
			return ResHelper.error("invalid type");
		}
		return ResHelper.success(genCode(opt, templateFileName, ""));
	}

	private String genCode(Base base, String templateFileName, String path) throws IOException, TemplateException {
		Configuration config = new Configuration(Configuration.VERSION_2_3_26);
		config.setDefaultEncoding("utf-8");
		TemplateLoader templateLoader = new SpringTemplateLoader(new DefaultResourceLoader(), "ftls" + path);
		config.setTemplateLoader(templateLoader);
		Template template = config.getTemplate(templateFileName, "utf-8");
		StringWriter stringWriter = new StringWriter();
		template.process(base, stringWriter);
		System.out.println(stringWriter.toString());
		return stringWriter.toString();
	}

}
