import {Injectable} from '@angular/core';
import {UserModel} from "../models/user.model";
import {BehaviorSubject, Observable, of, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private _currentUser$ = new BehaviorSubject<UserModel>(null);
  currentUser$ = this._currentUser$.asObservable();

  constructor(private http: HttpClient) {
  }

  loadCurrentUserByUserId(userId: string): Subscription {
    if (userId == "1") {
      this._currentUser$.next(this.getTim());
    } else {
      this._currentUser$.next(this.getTom());
    }
    return Subscription.EMPTY;
  }

  findAllUsers(): Observable<UserModel[]> {
    return this.http
        .get<UserModel[]>("./api/users");
  }


  private getTim(): UserModel {
    const user = new UserModel();
    user.firstName = "Tim";
    user.lastName = "Dodd";
    user.userId = "1";
    return user;
  }

  private getTom(): UserModel {
    const user = new UserModel();
    user.firstName = "Tom";
    user.lastName = "Dodd";
    user.userId = "2";
    return user;
  }


}
