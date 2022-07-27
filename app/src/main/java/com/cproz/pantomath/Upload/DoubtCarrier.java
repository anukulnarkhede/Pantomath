package com.cproz.pantomath.Upload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoubtCarrier {

    public String Name, Email, AnsPhotoUrl1, AnsPhotoUrl2, AnsText, AudioUrl, Chapter, FileUrl, Link, Photo1url, Photo2url, ProfileImageURL, Status,
            QText, Board, STD, Uid, Subject, Teacher, Created, TeacherImageUrl,TeacherEmail,Address, Institute, Branch;
    public Date DateTime, QuestionDate;
    public List<String> DoubtImages, AnswerImages;

    public DoubtCarrier(String name, String email, String ansPhotoUrl1, String ansPhotoUrl2, String ansText, String audioUrl,
                        String chapter, String fileUrl, String link, String photo1url, String photo2url, String profileImageURL,
                        String status, String text, String board, String STD, String uid, String subject, String teacher,
                        String created, Date dateTime, String TeacherImageUrl, Date QuestionDate, String TeacherEmail, String Address,
                        String Institute, String Branch, List<String> DoubtImages, List<String> AnswerImages
                        ){
        Name = name;
        Email = email;
        AnsPhotoUrl1 = ansPhotoUrl1;
        AnsPhotoUrl2 = ansPhotoUrl2;
        AnsText = ansText;
        AudioUrl = audioUrl;
        Chapter = chapter;
        FileUrl = fileUrl;
        Link = link;
        Photo1url = photo1url;
        Photo2url = photo2url;
        ProfileImageURL = profileImageURL;
        Status = status;
        QText = text;
        Board = board;
        this.STD = STD;
        Uid = uid;
        Subject = subject;
        Teacher = teacher;

        Created = created;
        DateTime = dateTime;
        this.TeacherImageUrl = TeacherImageUrl;
        this.QuestionDate = QuestionDate;
        this.TeacherEmail = TeacherEmail;
        this.Address = Address;
        this.Institute = Institute;
        this.Branch = Branch;
        this.DoubtImages = DoubtImages;
        this.AnswerImages = AnswerImages;
    }

}
