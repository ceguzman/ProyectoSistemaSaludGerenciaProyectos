import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 26251,
  login: '20',
};

export const sampleWithPartialData: IUser = {
  id: 26456,
  login: '@NQ',
};

export const sampleWithFullData: IUser = {
  id: 9065,
  login: 'pCge',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
