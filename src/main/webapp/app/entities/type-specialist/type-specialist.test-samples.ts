import { ITypeSpecialist, NewTypeSpecialist } from './type-specialist.model';

export const sampleWithRequiredData: ITypeSpecialist = {
  id: 20033,
  specialistType: 'apologise curry ew',
  stateSpecialist: 'ACTIVE',
};

export const sampleWithPartialData: ITypeSpecialist = {
  id: 17834,
  specialistType: 'limply',
  stateSpecialist: 'ACTIVE',
};

export const sampleWithFullData: ITypeSpecialist = {
  id: 25256,
  specialistType: 'aw',
  stateSpecialist: 'ACTIVE',
};

export const sampleWithNewData: NewTypeSpecialist = {
  specialistType: 'delightful till vivaciously',
  stateSpecialist: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
