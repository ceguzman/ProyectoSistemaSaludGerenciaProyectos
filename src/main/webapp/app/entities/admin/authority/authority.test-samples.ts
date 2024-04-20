import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'fea53e63-8bd3-49e9-9c98-b52cd0bb4192',
};

export const sampleWithPartialData: IAuthority = {
  name: 'cdd553ca-50ee-4976-9943-04f316b0fb65',
};

export const sampleWithFullData: IAuthority = {
  name: 'd1e17378-e422-4608-941f-86e251580410',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
