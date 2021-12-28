package com.cproz.pantomath.Signup;


import java.util.Date;

public class SignupInfoCarrier {

    public String
            Name,
            Email,
            Number,
            Address,
            User,
            Board,
            Class,
            uid,
            profileURL,
            AcademicYear,
            Token,
            Institute,
            Branch;

    public int NumberOfDoubtsAsked;
    public Date SignupTime;

    public SignupInfoCarrier(String name,
                             String email,
                             String number,
                             String address,
                             String user,
                             String Board,
                             String Class,
                             String uid,
                             String profileURL,
                             Date SignupTime,
                             int NumberOfDoubtsAsked,
                             String AcademicYear,
                             String Token, String Institute, String Branch) {
        this.Name = name;
        this.Email = email;
        this.Number = number;
        this.Address = address;
        this.Class = Class;
        this.Board = Board;
        this.User = user;
        this.uid = uid;
        this.profileURL = profileURL;
        this.SignupTime = SignupTime;
        this.NumberOfDoubtsAsked = NumberOfDoubtsAsked;
        this.AcademicYear = AcademicYear;
        this.Token = Token;
        this.Institute = Institute;
        this.Branch = Branch;

    }

}
