import dayjs from 'dayjs/esm';

import { IClinicHistory, NewClinicHistory } from './clinic-history.model';

export const sampleWithRequiredData: IClinicHistory = {
  id: 2118,
  dateClinic: dayjs('2024-04-20'),
};

export const sampleWithPartialData: IClinicHistory = {
  id: 29613,
  dateClinic: dayjs('2024-04-20'),
};

export const sampleWithFullData: IClinicHistory = {
  id: 11648,
  dateClinic: dayjs('2024-04-20'),
};

export const sampleWithNewData: NewClinicHistory = {
  dateClinic: dayjs('2024-04-19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
