import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-user-emails',
  templateUrl: './user-emails.component.html',
  styleUrls: ['./user-emails.component.scss']
})
export class UserEmailsComponent implements OnInit {

  users$ = this.userService.findAllUsers();
  constructor(private userService : UserService,) { }

  ngOnInit() {
  }

}
