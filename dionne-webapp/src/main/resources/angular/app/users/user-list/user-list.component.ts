import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {UserModel} from "../../models/user.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent {

  users$ = this.userService.findAllUsers();

  constructor(private userService: UserService,
              private router: Router) {
  }
  
  navigateToUserDetails(user: UserModel) {
    this.router.navigateByUrl(user.userId);
  }

  addNewUser(){
      this.router.navigateByUrl();
  }

}
