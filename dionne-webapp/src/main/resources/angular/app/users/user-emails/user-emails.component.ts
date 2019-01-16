import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {UserService} from "../../services/user.service";
import {EmailModel} from "../../models/email.model";
import {Observable} from "rxjs";
import {EmailService} from "../../services/email.service";
import {RemoveEmailComponent} from "../remove-email/remove-email.component";
import { NgModel } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {EmailOptionsComponent} from "../email-options/email-options.component";

@Component({
  selector: 'app-user-emails',
  templateUrl: './user-emails.component.html',
  styleUrls: ['./user-emails.component.scss']
})
export class UserEmailsComponent implements OnInit{
  private emails$ = this.emailService.currentEmailList$;
  private emailSelected : EmailModel;
  private _userId : string;
  @Input()
  set userId(userId: string) {
    this._userId = (userId && userId.trim()) || '<No User Id>';
  }
  get userId(){
    return this._userId;
  }
  constructor(private emailService : EmailService,
    public modalService: NgbModal) {
  }
  ngOnInit() {
    this.emailService.loadCurrentUsersEmail(this._userId);
  }
  openModal(emailToOpen : EmailModel){
    const modalRef = this.modalService.open(EmailOptionsComponent);
    modalRef.componentInstance.email = emailToOpen;
    modalRef.result.then(()=>{
      this.emailService.loadCurrentUsersEmail(this._userId);
    });
  }
}
