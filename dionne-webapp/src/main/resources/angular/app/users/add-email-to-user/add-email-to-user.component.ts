import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from "@angular/forms";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EmailModel} from "../../models/email.model";
import {EmailService} from "../../services/email.service";

@Component({
  selector: 'app-add-email-to-user',
  templateUrl: './add-email-to-user.component.html',
  styleUrls: ['./add-email-to-user.component.scss']
})
export class AddEmailToUserComponent implements OnInit {

  @Input() passedUserId : string;

  constructor(private formBuilder: FormBuilder,
              private emailService: EmailService,
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
    console.log(this.formGroup.get('isPrimary'));
    const emailToSave = this.formGroup.getRawValue() as EmailModel;
    emailToSave.userId = this.passedUserId;
    console.log(emailToSave);
    this.emailService.saveEmailToUser(this.passedUserId, emailToSave);
    this.modalService.dismissAll();
  }

  get f() { return this.formGroup.controls; }

  private createFormGroup(): FormGroup {
    return this.formBuilder.group({
      emailId : '',
      userId : '',
      emailAddress : ['',[Validators.email,Validators.required]],
      isPrimary : new FormControl(false),
      isVerified : new FormControl(false),
    });
  }
}
