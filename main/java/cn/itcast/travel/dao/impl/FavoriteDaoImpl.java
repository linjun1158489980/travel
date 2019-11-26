package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.FavoritCount;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";

        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";

        template.update(sql,rid,new Date(),uid);
    }

    @Override
    public void del(int rid, int uid) {
        String sql = "DELETE FROM tab_favorite WHERE uid=? AND rid=?";

        template.update(sql,uid,rid);
    }





    @Override
    public boolean searchcount(int rid) {
        int flag=0;
        String sql="SELECT count(*) FROM tab_favoritenum WHERE rid=?";
        flag=template.queryForInt(sql,rid);
        if (flag>0){
            return true;
        }
       else {
           return false;
        }
    }

    @Override
    public void insertCount(int rid) {
        String sql="INSERT INTO tab_favoritenum VALUES(?, 1)";
        template.update(sql,rid);
    }

    @Override
    public void updateCount(int rid) {
         String sql="update tab_favoritenum set number=number+1 where rid=?";
         template.update(sql,rid);
    }

    @Override
    public void deleteCount(int rid) {
        String sql="update tab_favoritenum set number=number-1 where rid=?";
        template.update(sql,rid);
    }

    @Override
    public List<Route> searchfavoriteCount() {
        String sql="SELECT * FROM tab_route  t1 RIGHT JOIN tab_favoritenum t2 on t1.rid=t2.rid ORDER BY t2.number DESC";
        List<Route> count=template.query(sql,new BeanPropertyRowMapper<Route>(Route.class));
        return count;
    }
}
