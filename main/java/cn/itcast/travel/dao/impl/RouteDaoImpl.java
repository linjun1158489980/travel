package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid,String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //1.定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }



        sql = sb.toString();
        System.out.println(sql);
        System.out.println(params.toArray());
        return template.queryForObject(sql,params.toArray(),Integer.class);

    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        //String sql = "select * from tab_route where cid = ? and rname like ?  limit ? , ?";
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");//分页条件


        sql = sb.toString();
        params.add(start);
        params.add(pageSize);


        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

    @Override
    public int findByrid(int rid) {
        String sql="select cid from tab_route where rid = ?";

        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public int findfaTotalCount(int uid) {
        String sql = "SELECT count(*)  FROM `tab_favorite` WHERE uid =?";
        return template.queryForObject(sql,Integer.class,uid);

    }

    @Override
    public List<Route> findfaByPage(int uid, int start, int pageSize) {
        String sql = " SELECT * FROM  `tab_route` WHERE rid IN (SELECT rid FROM `tab_favorite` WHERE uid =?)";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        if(uid != 0){
            params.add(uid);//添加？对应的值
        }
        sb.append(" limit ? , ? ");//分页条件
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        System.out.println(sql);
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());


    }
}
