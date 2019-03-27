package h2o.common.thirdparty.freemarker.model;


import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import h2o.common.util.json.JsonUtil;

import java.util.List;

public class ParseJsonMethod implements TemplateMethodModelEx {

	private volatile JsonUtil jsonUtil = new JsonUtil();

	public void setJsonUtil(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {

		String jstr = (String) args.get(0);

		if (jstr != null) {
			jstr = jstr.trim();
			if (!"".equals(jstr) && !"null".equals(jstr)) {
				return this.jsonUtil .json2Map(jstr);
			}
		}

		return NullModel.NULLMODEL;

	}

}