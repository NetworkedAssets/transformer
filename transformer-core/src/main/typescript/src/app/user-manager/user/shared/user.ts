export class User {
  static fromObject(obj: any) {
    return new User(obj.username, '');
  }

  static copyOf(user: User) {
    return new User(user.username, user.password);
  }

  constructor(public username: string,
              public password: string) {
  }
}
