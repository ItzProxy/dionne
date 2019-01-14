import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {UserModel} from "../../models/user.model";
import {EmailModel} from "../../models/email.model";

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
  private closeResult: string;
  formGroup: FormGroup = this.createFormGroup();

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

  saveForm(): void {
    const emailToSave = this.formGroup.getRawValue() as EmailModel;
    emailToSave.userId = this.passedUserId;
    console.log(emailToSave);
    console.log(this.userService.saveEmailToUser(this.passedUserId, emailToSave).subscribe());
  }

  private createFormGroup(): FormGroup {
    return this.formBuilder.group({
      "emailId": '',
      "userId": '',
      "emailAddress": '',
      "isPrimary": false,
      "isVerified": false,
    });
  }
}
