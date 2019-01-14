import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {UserService} from "../../services/user.service";
import {EmailModel} from "../../models/email.model";

@Component({
  selector: 'app-user-emails',
  templateUrl: './user-emails.component.html',
  styleUrls: ['./user-emails.component.scss']
})
export class UserEmailsComponent implements OnInit{
  private emails$ = this.userService.currentEmailList$;

  private _userId : string;
  @Input()
  set userId(userId: string) {
    this._userId = (userId && userId.trim()) || '<No User Id>';
  }
  get userId(){
    return this._userId;
  }
  constructor(private userService : UserService) {

  }

  ngOnInit() {
    console.log(this._userId);
    this.userService.loadCurrentUsersEmail(this._userId);
  }
}
