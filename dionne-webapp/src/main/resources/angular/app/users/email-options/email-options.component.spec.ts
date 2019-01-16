import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailOptionsComponent } from './email-options.component';

describe('EmailOptionsComponent', () => {
  let component: EmailOptionsComponent;
  let fixture: ComponentFixture<EmailOptionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmailOptionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
