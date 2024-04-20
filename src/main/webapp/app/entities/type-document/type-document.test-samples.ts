import { ITypeDocument, NewTypeDocument } from './type-document.model';

export const sampleWithRequiredData: ITypeDocument = {
  id: 10284,
  initials: 'beautifully revoluti',
  documentName: 'road yippee',
  stateTypeDocument: 'ACTIVE',
};

export const sampleWithPartialData: ITypeDocument = {
  id: 26093,
  initials: 'battle modulo towel',
  documentName: 'steel',
  stateTypeDocument: 'ACTIVE',
};

export const sampleWithFullData: ITypeDocument = {
  id: 17954,
  initials: 'salaam',
  documentName: 'flustered',
  stateTypeDocument: 'ACTIVE',
};

export const sampleWithNewData: NewTypeDocument = {
  initials: 'deceivingly',
  documentName: 'grown',
  stateTypeDocument: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
