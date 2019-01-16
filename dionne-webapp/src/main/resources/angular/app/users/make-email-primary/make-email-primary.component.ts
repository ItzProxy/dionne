import {Component, Input, OnInit} from '@angular/core';
import {EmailModel} from "../../models/email.model";
import {EmailService} from "../../services/email.service";
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";


@Component({
  selector: 'app-make-email-primary',
  templateUrl: './make-email-primary.component.html',
  styleUrls: ['./make-email-primary.component.scss']
})
export class MakeEmailPrimaryComponent {

  @Input('email')
    email : EmailModel;

  constructor(private modalService: NgbModal,
    private emailService : EmailService,
    public activeModal: NgbActiveModal) { }

  makeEmailPrimary(){
    this.emailService.makeEmailPrimary(this.email.userId,this.email.emailId);
    this.activeModal.close();
  }

}
