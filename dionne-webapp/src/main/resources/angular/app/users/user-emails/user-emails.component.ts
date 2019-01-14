import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {UserService} from "../../services/user.service";
import {EmailModel} from "../../models/email.model";

@Component({
  selector: 'app-user-emails',
  templateUrl: './user-emails.component.html',
  styleUrls: ['./user-emails.component.scss']
})
export class UserEmailsComponent implements OnInit, OnChanges{
  @Input() passedUserId: string;
  emails$ : EmailModel[];
  constructor(private userService : UserService,) {
    console.log(this.passedUserId);
    this.userService.getUsersEmail(this.passedUserId).subscribe(emails =>{
      this.emails$ = emails;
    });
  }

  ngOnInit() {
  }
  ngOnChanges(changes: SimpleChanges): void {
    
  }
}
