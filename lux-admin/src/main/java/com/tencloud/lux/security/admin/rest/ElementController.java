package com.tencloud.lux.security.admin.rest;

import com.tencloud.lux.security.common.msg.ObjectRestResponse;
import com.tencloud.lux.security.common.msg.TableResultResponse;
import com.tencloud.lux.security.common.rest.BaseController;
import com.tencloud.lux.security.admin.biz.ElementBiz;
import com.tencloud.lux.security.admin.biz.UserBiz;
import com.tencloud.lux.security.admin.entity.Element;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Controller
@RequestMapping("element")
public class ElementController extends BaseController<ElementBiz, Element> {
  @Autowired
  private UserBiz userBiz;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @ResponseBody
  public TableResultResponse<Element> page(@RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "1") int offset,String name, @RequestParam(defaultValue = "0") int menuId) {
    Example example = new Example(Element.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("menuId", menuId);
    if(StringUtils.isNotBlank(name)){
      criteria.andLike("name", "%" + name + "%");
    }
    List<Element> elements = baseBiz.selectByExample(example);
    return new TableResultResponse<Element>(elements.size(), elements);
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseBody
  public ObjectRestResponse<Element> getAuthorityElement(String menuId) {
    int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
    List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "",menuId);
    return new ObjectRestResponse<List<Element>>().data(elements);
  }

  @RequestMapping(value = "/user/menu", method = RequestMethod.GET)
  @ResponseBody
  public ObjectRestResponse<Element> getAuthorityElement() {
    int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
    List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "");
    return new ObjectRestResponse<List<Element>>().data(elements);
  }
}
