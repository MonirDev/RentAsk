package com.teamdakatia.rentask;

public class AddData {
    private String name, phone_number, password, imgUrl1, imgUrl2, imgUrl3,
                   home_type, price,nRooms, nBath, division, district, area_name,
                   short_address, pick_lat, pick_long,post_phone_number,
                   rent_start, check_value ;
    public AddData(){

    }

    public AddData(String name, String phone_number, String password) {
        this.name = name;
        this.phone_number = phone_number;
        this.password = password;
    }

    public AddData(String imgUrl1, String imgUrl2, String imgUrl3, String home_type, String price,
                   String nRooms, String nBath, String division, String district,
                   String area_name, String short_address, String pick_lat, String pick_long,
                   String post_phone_number, String rent_start, String check_value) {
        this.imgUrl1 = imgUrl1;
        this.imgUrl2 = imgUrl2;
        this.imgUrl3 = imgUrl3;
        this.home_type = home_type;
        this.price = price;
        this.nRooms = nRooms;
        this.nBath = nBath;
        this.division = division;
        this.district = district;
        this.area_name = area_name;
        this.short_address = short_address;
        this.pick_lat = pick_lat;
        this.pick_long = pick_long;
        this.post_phone_number = post_phone_number;
        this.rent_start = rent_start;
        this.check_value = check_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getHome_type() {
        return home_type;
    }

    public void setHome_type(String home_type) {
        this.home_type = home_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getnRooms() {
        return nRooms;
    }

    public void setnRooms(String nRooms) {
        this.nRooms = nRooms;
    }

    public String getnBath() {
        return nBath;
    }

    public void setnBath(String nBath) {
        this.nBath = nBath;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getShort_address() {
        return short_address;
    }

    public void setShort_address(String short_address) {
        this.short_address = short_address;
    }

    public String getPick_lat() {
        return pick_lat;
    }

    public void setPick_lat(String pick_lat) {
        this.pick_lat = pick_lat;
    }

    public String getPick_long() {
        return pick_long;
    }

    public void setPick_long(String pick_long) {
        this.pick_long = pick_long;
    }

    public String getPost_phone_number() {
        return post_phone_number;
    }

    public void setPost_phone_number(String post_phone_number) {
        this.post_phone_number = post_phone_number;
    }

    public String getRent_start() {
        return rent_start;
    }

    public void setRent_start(String rent_start) {
        this.rent_start = rent_start;
    }

    public String getCheck_value() {
        return check_value;
    }

    public void setCheck_value(String check_value) {
        this.check_value = check_value;
    }
}
