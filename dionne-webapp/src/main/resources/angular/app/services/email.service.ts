import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, Subscription, pipe} from "rxjs";
import {EmailModel} from "../models/email.model";
import {HttpClient} from "@angular/common/http";
import { delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EmailService {


  private _currentEmailList$ = new BehaviorSubject<EmailModel[]>(null);
  currentEmailList$ = this._currentEmailList$.asObservable();

  constructor(private http : HttpClient) { }


  loadCurrentUsersEmail(userId : string): Subscription {
    return this.getUsersEmail(userId).subscribe((emailList : EmailModel[])=>{
      this._currentEmailList$.next(emailList);
    });
  }

  getUsersEmail(userId : string) : Observable<EmailModel[]>{
    return this.http
        .get<EmailModel[]>(
          "./api/users/" +userId + "/emails"
        );
  }

  saveEmailToUser(userId : string, emailModel : EmailModel) : Subscription{
    return this.http
        .post<EmailModel>(
            "./api/users/" + userId + "/emails", emailModel
        ).pipe(
          delay(200)
        ).subscribe(()=>{
          this.loadCurrentUsersEmail(userId);
        });
  }

  deleteEmail(email : EmailModel) : Subscription {
    return this.http
        .delete(
        `./api/users/${email.userId}/emails/${email.emailId}`
    ).pipe(
      delay(2000)
    )
    .subscribe((result)=>{
      this.loadCurrentUsersEmail(email.userId);
    });
  }

  makeEmailPrimary(userId : string, emailId : string) : Subscription{
    return this.http
        .post(
            `./api/users/${userId}/emails/${emailId}/primary`, null
        ).pipe(
            delay(1000)
        )
        .subscribe((result)=>{
          this.loadCurrentUsersEmail(userId);
        });
  }

  
}
