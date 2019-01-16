import {Component, Input, OnInit} from '@angular/core';
import {EmailService} from "../../services/email.service";
import {EmailModel} from "../../models/email.model";

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent {

  @Input('email')
  email : EmailModel;

  constructor(emailService : EmailService) { }
  //TODO
  verifyEmail() {
    console.log("Not implemented yet");
  }

}
