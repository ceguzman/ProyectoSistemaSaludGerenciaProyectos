import dayjs from 'dayjs/esm';

import { IMedicalAuthorization, NewMedicalAuthorization } from './medical-authorization.model';

export const sampleWithRequiredData: IMedicalAuthorization = {
  id: 2835,
  detailAuthorization: 'plume always',
  stateAuthorization: 'INACTIVE',
  dateAuthorization: dayjs('2024-04-20'),
};

export const sampleWithPartialData: IMedicalAuthorization = {
  id: 30689,
  detailAuthorization: 'show whose',
  stateAuthorization: 'ACTIVE',
  dateAuthorization: dayjs('2024-04-20'),
};

export const sampleWithFullData: IMedicalAuthorization = {
  id: 8494,
  detailAuthorization: 'detailed',
  stateAuthorization: 'INACTIVE',
  dateAuthorization: dayjs('2024-04-20'),
};

export const sampleWithNewData: NewMedicalAuthorization = {
  detailAuthorization: 'save carefully amongst',
  stateAuthorization: 'ACTIVE',
  dateAuthorization: dayjs('2024-04-20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
