import dayjs from 'dayjs/esm';

import { IMedicalAppointments, NewMedicalAppointments } from './medical-appointments.model';

export const sampleWithRequiredData: IMedicalAppointments = {
  id: 3737,
  dateMedical: dayjs('2024-04-20'),
};

export const sampleWithPartialData: IMedicalAppointments = {
  id: 24634,
  dateMedical: dayjs('2024-04-20'),
};

export const sampleWithFullData: IMedicalAppointments = {
  id: 27746,
  dateMedical: dayjs('2024-04-20'),
};

export const sampleWithNewData: NewMedicalAppointments = {
  dateMedical: dayjs('2024-04-20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
