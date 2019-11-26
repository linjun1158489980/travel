package cn.itcast.travel.service.impl;

public class test {
    public static void main(String[] args) {
        RouteServiceImpl rs=new RouteServiceImpl();

        System.out.println( rs.findOneCategory("1").getCname());

    }
}
