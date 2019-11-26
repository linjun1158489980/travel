package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.FavoritCount;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;//如果对象有值，则为true，反之，则为false
    }

    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
       Boolean b= favoriteDao.searchcount(Integer.parseInt(rid));
       if(b){
           System.out.println("里面有哦");
           favoriteDao.updateCount(Integer.parseInt(rid));
       }else{
           favoriteDao.insertCount(Integer.parseInt(rid));
           System.out.println("没有");
       }

    }

    @Override
    public void del(String rid, int uid) {
        favoriteDao.del(Integer.parseInt(rid),uid);
        favoriteDao.deleteCount(Integer.parseInt(rid));
    }

    @Override
    public List<Route> searchfavoriteCount() {
       List<Route> count=favoriteDao.searchfavoriteCount();
        return count;
    }



}
