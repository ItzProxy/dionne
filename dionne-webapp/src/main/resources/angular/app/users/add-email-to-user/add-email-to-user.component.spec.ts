import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEmailToUserComponent } from './add-email-to-user.component';

describe('AddEmailToUserComponent', () => {
  let component: AddEmailToUserComponent;
  let fixture: ComponentFixture<AddEmailToUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddEmailToUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddEmailToUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
