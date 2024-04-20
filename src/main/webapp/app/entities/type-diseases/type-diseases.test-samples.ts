import { ITypeDiseases, NewTypeDiseases } from './type-diseases.model';

export const sampleWithRequiredData: ITypeDiseases = {
  id: 31635,
  diseasesType: 'riding',
};

export const sampleWithPartialData: ITypeDiseases = {
  id: 10680,
  diseasesType: 'utterly drug',
};

export const sampleWithFullData: ITypeDiseases = {
  id: 22855,
  diseasesType: 'around er starchy',
};

export const sampleWithNewData: NewTypeDiseases = {
  diseasesType: 'fake',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
