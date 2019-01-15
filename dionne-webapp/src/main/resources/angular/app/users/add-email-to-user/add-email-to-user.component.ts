import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {UserModel} from "../../models/user.model";
import {EmailModel} from "../../models/email.model";
import { validateConfig } from '@angular/router/src/config';

@Component({
  selector: 'app-add-email-to-user',
  templateUrl: './add-email-to-user.component.html',
  styleUrls: ['./add-email-to-user.component.scss']
})
export class AddEmailToUserComponent implements OnInit {

  @Input() passedUserId : string;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private modalService: NgbModal) { }

  ngOnInit() {
  }
  submitted : Boolean = false;
  formGroup: FormGroup = this.createFormGroup();

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
  }

  saveForm(): void {
    this.submitted = true;
    if(this.formGroup.invalid){
      return;
    }
    const emailToSave = this.formGroup.getRawValue() as EmailModel;
    emailToSave.userId = this.passedUserId;
    console.log(emailToSave);
    console.log(this.userService.saveEmailToUser(this.passedUserId, emailToSave).subscribe(()=>{
    }));
    this.modalService.dismissAll();
  }

  get f() { return this.formGroup.controls; }

  private createFormGroup(): FormGroup {
    return this.formBuilder.group({
      emailId : '',
      userId : '',
      emailAddress : ['',[Validators.email,Validators.required]],
      isPrimary : false,
      isVerified : false,
    });
  }
}
