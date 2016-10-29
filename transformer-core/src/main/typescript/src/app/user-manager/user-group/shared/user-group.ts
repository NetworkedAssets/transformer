import {User} from '../../user/shared/user';
export class UserGroup {

  static fromObject(obj: any) {
    return new UserGroup(obj.name,
      obj.automaticallyCreated,
      obj.manualUserAssignementAllowed,
      obj.roles,
      (obj.users || []).map((x: any) => User.fromObject(x)),
      obj.id);
  }

  static copyOf(group: UserGroup) {
    return new UserGroup(
      group.name,
      group.automaticallyCreated,
      group.manualUserAssignementAllowed,
      group.roles.slice(),
      group.users.slice().map(u => User.copyOf(u)),
      group.id);
  }

  constructor(public name: string,
              public automaticallyCreated: boolean,
              public manualUserAssignementAllowed: boolean,
              public roles: Array<string>,
              public users: Array<User>,
              public id?: number) {
  }

}
