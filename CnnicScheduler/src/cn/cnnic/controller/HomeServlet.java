package cn.cnnic.controller;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet
  extends HttpServlet
{
  private Configuration configuration = null;
  
  public void destroy()
  {
    super.destroy();
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String template = "index.html";
    response.setCharacterEncoding("UTF-8");
    response.setHeader("content-type", "text/html;charset=UTF-8");
    if (request.getParameter("template") != null) {
      template = request.getParameter("template");
      if(template.contains("../")) {
    	  return;
      }
    }
    ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "/");
    TemplateLoader tl = this.configuration.getTemplateLoader();
    TemplateLoader[] loaders = { tl, ctl };
    MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
    this.configuration.setTemplateLoader(mtl);
    Template temp = this.configuration.getTemplate(template, "utf-8");
    Writer writer = response.getWriter();
    try
    {
      temp.process(null, writer);
      String htmlStr = writer.toString();
      System.out.println(htmlStr);
    }
    catch (TemplateException e)
    {
      e.printStackTrace();
    }
  }
  
  public void init()
    throws ServletException
  {
    this.configuration = new Configuration();
    this.configuration.setServletContextForTemplateLoading(getServletContext(), "");
  }
}
