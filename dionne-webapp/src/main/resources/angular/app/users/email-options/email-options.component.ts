import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {EmailService} from "../../services/email.service";
import {EmailModel} from "../../models/email.model";

@Component({
  selector: 'app-email-options',
  templateUrl: './email-options.component.html',
  styleUrls: ['./email-options.component.scss']
})
export class EmailOptionsComponent {
  @ViewChild('openModal') openModal:ElementRef;

  @Input()
    email : EmailModel;

  constructor(public activeModal: NgbActiveModal) { }
}
