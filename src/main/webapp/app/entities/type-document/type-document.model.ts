import { State } from 'app/entities/enumerations/state.model';

export interface ITypeDocument {
  id: number;
  initials?: string | null;
  documentName?: string | null;
  stateTypeDocument?: keyof typeof State | null;
}

export type NewTypeDocument = Omit<ITypeDocument, 'id'> & { id: null };
