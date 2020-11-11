package com.example.squatchallenge;

public class User {
    public String username;
    public long speed_time;
    public long total_count;
    public String team_name;
    public User(){}

    public User(String username){
        this.username = username;
        this.speed_time = 180000;
        this.total_count = 0;
        this.team_name = "";
    }
}