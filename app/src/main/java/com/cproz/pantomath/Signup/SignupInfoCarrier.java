package com.cproz.pantomath.Signup;


public class SignupInfoCarrier {

    public String Name, Email, Number, Address, User, Board, Class, uid, profileURL;

    public SignupInfoCarrier(String name, String email, String number, String address, String user, String Board, String Class, String uid, String profileURL) {
        this.Name = name;
        this.Email = email;
        this.Number = number;
        this.Address = address;
        this.Class = Class;
        this.Board = Board;
        this.User = user;
        this.uid = uid;
        this.profileURL = profileURL;

    }

}
