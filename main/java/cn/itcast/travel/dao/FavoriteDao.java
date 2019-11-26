package cn.itcast.travel.dao;

import cn.itcast.travel.domain.FavoritCount;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteDao {

    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据rid 查询收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param i
     * @param uid
     */
    void add(int i, int uid);

    void del(int i, int uid);



    boolean searchcount(int parseInt);

    void insertCount(int parseInt);

    void updateCount(int parseInt);

    void deleteCount(int parseInt);

   List<Route> searchfavoriteCount();
}
