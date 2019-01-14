import {Injectable} from '@angular/core';
import {UserModel} from "../models/user.model";
import {BehaviorSubject, Observable, of, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import { map, filter, catchError, mergeMap } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private _currentUser$ = new BehaviorSubject<UserModel>(null);
  currentUser$ = this._currentUser$.asObservable();

  constructor(private http: HttpClient) {
  }

  loadCurrentUserByUserId(userId: string): Subscription {

      this.findUserById(userId).subscribe((user : UserModel) => {
        this._currentUser$.next(user);
      });
      return Subscription.EMPTY;
    }
  
  findAllUsers(): Observable<UserModel[]> {
    return this.http
        .get<UserModel[]>("./api/users/");
  }

  findUserById(userId: string): Observable<UserModel> {
    return this.http
      .get<UserModel>("./api/users/" + userId);
  }

  saveCurrentUser(userModel : UserModel): Observable<UserModel>{
    return this.http
        .put<UserModel>("./api/users/" + userModel.userId, userModel);
  }

  createNewUser(userModel : UserModel) : Observable<UserModel>{
    return this.http
        .post<UserModel>(
            "./api/users/", userModel);
  }
}
