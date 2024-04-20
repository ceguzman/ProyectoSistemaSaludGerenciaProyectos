import { IMedicationRequest, NewMedicationRequest } from './medication-request.model';

export const sampleWithRequiredData: IMedicationRequest = {
  id: 27757,
  name: 'drat',
  amount: 7,
};

export const sampleWithPartialData: IMedicationRequest = {
  id: 27605,
  name: 'secret',
  amount: 0,
  milligrams: 7,
};

export const sampleWithFullData: IMedicationRequest = {
  id: 2842,
  name: 'gouge',
  amount: 9,
  milligrams: 7,
};

export const sampleWithNewData: NewMedicationRequest = {
  name: 'wheat',
  amount: 3,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
