package com.cproz.pantomath.Upload;

import java.util.Date;

public class DoubtCarrier {

    public String Name, Email, AnsPhotoUrl1, AnsPhotoUrl2, AnsText, AudioUrl, Chapter, FileUrl, Link, Photo1url, Photo2url, ProfileImageURL, Status,
            QText, Board, STD, Uid, Subject, Teacher, Created, TeacherImageUrl,TeacherEmail;
    public Date DateTime, QuestionDate;

    public DoubtCarrier(String name, String email, String ansPhotoUrl1, String ansPhotoUrl2, String ansText, String audioUrl,
                        String chapter, String fileUrl, String link, String photo1url, String photo2url, String profileImageURL,
                        String status, String text, String board, String STD, String uid, String subject, String teacher,
                        String created, Date dateTime, String TeacherImageUrl, Date QuestionDate, String TeacherEmail){
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
    }

}
