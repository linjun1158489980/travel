package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*") // /user/add /user/find
public class UserServlet extends BaseServlet {

    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String[]> map = request.getParameterMap();
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        //验证校验
        String [] check ;

        check=map.get("check");
        //从sesion中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较
        for (int i=0;i<check.length;i++){
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check[i])){
                //验证码错误
                //注册失败
                info.setFlag(false);
                info.setErrorMsg("验证码错误");
                response.setContentType("application/json;charset=utf-8");
                mapper.writeValue(response.getOutputStream(),info);
                return;
                //将info对象序列化为json
            }
            System.out.println(check[i]);
        }

        //1.获取数据


        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        //UserService service = new UserServiceImpl();
        System.out.println(user);
        boolean flag = service.regist(user);

        //4.响应结果
        if(flag){
            //注册成功
            info.setFlag(true);
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }

        //将info对象序列化为json

        String json = mapper.writeValueAsString(info);

        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    public void registUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
          boolean flag=false;
          flag=service.registUsername(username);
          PrintWriter pw = response.getWriter();

          pw.println(flag);



    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();

        String [] check ;
       check=map.get("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        System.out.println(checkcode_server);
        ResultInfo info = new ResultInfo();

        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用Service查询
       // UserService service = new UserServiceImpl();
        User u  = service.login(user);

        ObjectMapper mapper = new ObjectMapper();

        //4.判断用户对象是否为null
        if(u == null){
            //用户名密码或错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),info);
            return;
        }
        //5.判断验证码是否正确
        for (int i=0;i<check.length;i++){
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check[i])){
                //验证码错误
                //注册失败
                info.setFlag(false);
                info.setErrorMsg("验证码错误");
                response.setContentType("application/json;charset=utf-8");
                mapper.writeValue(response.getOutputStream(),info);
                return;
                //将info对象序列化为json
            }
            System.out.println(check[i]);
        }
        if(u != null && !"Y".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),info);
            return;
        }
        //6.判断登录成功

        if(u != null && "Y".equals(u.getStatus())){
            request.getSession().setAttribute("user",u);//登录成功标记

            //登录成功
            info.setFlag(true);
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),info);
        }

        //响应数据



    }

    /**
     * 查询单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端

       /* ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);*/
       writeValue(user,response);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();

        //2.跳转登录页面
        response.sendRedirect(request.getContextPath()+"/index.html");
    }

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if(code != null){
            //2.调用service完成激活
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判断标记
            String msg = null;
            if(flag){
                //激活成功
                msg = "激活成功，请<a href='http://localhost:8080/travel/login.html'>登录</a>";
            }else{
                //激活失败
                msg = "激活失败，请联系管理员!";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
//    /**
//     * 查询我的收藏功能
//     * @param request
//     * @param response
//     * @throws ServletException
//     * @throws IOException
//     */
//    public void searchCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//                 String uidStr=request.getParameter("rid");
//                 System.out.println(uidStr);
//                request.setCharacterEncoding("UTF-8");
//                String currentPageStr = request.getParameter("currentPage");
//                String pageSizeStr = request.getParameter("pageSize");
//        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
//        if(currentPageStr != null && currentPageStr.length() > 0){
//            currentPage = Integer.parseInt(currentPageStr);
//        }else{
//            currentPage = 1;
//        }
//
//        PageBean<Route> pb = routeService.pageQuery2(currentPage, pageSize,uidStr);
//
//
//
//    }
}
