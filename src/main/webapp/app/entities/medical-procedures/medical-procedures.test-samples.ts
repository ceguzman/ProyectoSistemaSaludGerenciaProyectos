import dayjs from 'dayjs/esm';

import { IMedicalProcedures, NewMedicalProcedures } from './medical-procedures.model';

export const sampleWithRequiredData: IMedicalProcedures = {
  id: 24849,
  typeProcedures: 'blah although bountiful',
  description: 'nearly where pish',
  dateProcedures: dayjs('2024-04-20'),
};

export const sampleWithPartialData: IMedicalProcedures = {
  id: 3352,
  typeProcedures: 'synthesise until um',
  description: 'minor phooey pfft',
  dateProcedures: dayjs('2024-04-20'),
};

export const sampleWithFullData: IMedicalProcedures = {
  id: 20881,
  typeProcedures: 'personal brook',
  description: 'reluctantly aggravating',
  dateProcedures: dayjs('2024-04-20'),
};

export const sampleWithNewData: NewMedicalProcedures = {
  typeProcedures: 'webmail',
  description: 'beggar',
  dateProcedures: dayjs('2024-04-20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
