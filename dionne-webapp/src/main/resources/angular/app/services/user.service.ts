import {Injectable} from '@angular/core';
import {UserModel} from "../models/user.model";
import {BehaviorSubject, Observable, of, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {EmailModel} from "../models/email.model";
import {delay} from "rxjs/operators";
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private _currentUser$ = new BehaviorSubject<UserModel>(null);
  currentUser$ = this._currentUser$.asObservable();

  private _allUsers$ = new BehaviorSubject<UserModel[]>([]);
  allUsers$ = this._allUsers$.asObservable();

  private _currentEmailList$ = new BehaviorSubject<EmailModel[]>(null);
  currentEmailList$ = this._currentEmailList$.asObservable();


  constructor(private http: HttpClient) {
  }

  loadCurrentUserByUserId(userId: string): Subscription {
      return this.findUserById(userId).subscribe((user : UserModel) => {
        this._currentUser$.next(user);
      });
    }

  loadAllUsers(): Subscription {
    return this.findAllUsers().subscribe((userList : UserModel[]) =>{
      this._allUsers$.next(userList);
    });
  }

  loadCurrentUsersEmail(userId : string): Subscription {
    return this.getUsersEmail(userId).subscribe((emailList : EmailModel[])=>{
      this._currentEmailList$.next(emailList);
    });
  }

  saveUser(userModel: UserModel, successCallback?: any): Subscription {
    if(userModel.userId) {
      return this.http
          .put<UserModel>("./api/users/" + userModel.userId, userModel)
          .pipe(
              delay(2000)
          )
          .subscribe(savedUser => {
            this._currentUser$.next(savedUser);
            if(successCallback) {
              successCallback(savedUser);
            }
          });
    } else {
      return this.http
          .post<UserModel>("./api/users/", userModel)
          .pipe(
              delay(2000)
          )
          .subscribe(savedUser => {
            this._currentUser$.next(savedUser);
            if(successCallback) {
              successCallback(savedUser);
            }
          });
    }
  }


  private findAllUsers(): Observable<UserModel[]> {
    return this.http
        .get<UserModel[]>("./api/users/");
  }

  private findUserById(userId: string): Observable<UserModel> {
    return this.http
      .get<UserModel>("./api/users/" + userId);
  }


  getUsersEmail(userId : string) : Observable<EmailModel[]>{
    return this.http
        .get<EmailModel[]>(
          "./api/users/" +userId + "/emails"
        );
  }

  saveEmailToUser(userId : string, emailModel : EmailModel) : Observable<EmailModel>{
    return this.http
        .post<EmailModel>(
            "./api/users/" + userId + "/emails", emailModel
        );
  }

  removeEmail(email : EmailModel) : Subscription {
    return this.http
        .delete(
        "./api/users/" + email.userId + "/emails/" + email.emailId
    ).subscribe(()=>console.log("finished deleting"));
  }
}
