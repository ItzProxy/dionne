import {Component, Input, OnInit} from '@angular/core';
import {EmailService} from "../../services/email.service";
import {EmailModel} from "../../models/email.model";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent {

  @Input('email')
  email : EmailModel;

  constructor(private emailService : EmailService,
    private activeModal : NgbActiveModal) { }
  //TODO
  verifyEmail() {
    this.emailService.sendVerifyEmail(this.email.userId,this.email.emailId);
    this.activeModal.close("Email Verification done");
  }

}
