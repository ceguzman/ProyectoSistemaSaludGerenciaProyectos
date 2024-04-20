import { IPeople, NewPeople } from './people.model';

export const sampleWithRequiredData: IPeople = {
  id: 11959,
  documentNumber: 'till',
  firstName: 'María de los Ángeles',
  firstSurname: 'aw officially',
};

export const sampleWithPartialData: IPeople = {
  id: 7912,
  documentNumber: 'throughout yippee',
  firstName: 'Cristina',
  firstSurname: 'programming pfft air',
  secondName: 'intensely inasmuch infantile',
  secondSurname: 'sharply consequently yuck',
};

export const sampleWithFullData: IPeople = {
  id: 21199,
  documentNumber: 'awkwardly',
  firstName: 'Ignacio',
  firstSurname: 'outside fox',
  secondName: 'merrily',
  secondSurname: 'gee sheepishly',
};

export const sampleWithNewData: NewPeople = {
  documentNumber: 'impeccable fast though',
  firstName: 'Rosa',
  firstSurname: 'wherever potentially garrotte',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
