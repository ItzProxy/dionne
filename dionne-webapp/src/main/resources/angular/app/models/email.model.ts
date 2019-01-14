export class EmailModel {
  constructor(public userId: string,
              public emailId: string,
              public emailAddress: string,
              public isPrimary: boolean,
              public isVerified: boolean) {
  }

}
