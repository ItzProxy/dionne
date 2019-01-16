import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {NgbModal, ModalDismissReasons, NgbActiveModal} from "@ng-bootstrap/ng-bootstrap"
import { EmailService } from '../../services/email.service';
import { EmailModel } from '../../models/email.model';

@Component({
  selector: 'app-remove-email',
  templateUrl: './remove-email.component.html',
  styleUrls: ['./remove-email.component.scss']
})
export class RemoveEmailComponent{

  @ViewChild('openModal') openModal:ElementRef;
  @Input('email')
    email : EmailModel;
  emailAddress : string;
  constructor(private modalService: NgbModal,
              private emailService : EmailService,
              public activeModal: NgbActiveModal) {
    }

  deleteEmail(){
    console.log(`Will remove ${this.email.emailAddress}`);
    this.activeModal.close(this.emailService.deleteEmail(this.email));
  }
}
