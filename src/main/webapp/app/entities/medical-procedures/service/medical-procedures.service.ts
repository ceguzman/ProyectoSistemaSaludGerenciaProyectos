import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicalProcedures, NewMedicalProcedures } from '../medical-procedures.model';

export type PartialUpdateMedicalProcedures = Partial<IMedicalProcedures> & Pick<IMedicalProcedures, 'id'>;

type RestOf<T extends IMedicalProcedures | NewMedicalProcedures> = Omit<T, 'dateProcedures'> & {
  dateProcedures?: string | null;
};

export type RestMedicalProcedures = RestOf<IMedicalProcedures>;

export type NewRestMedicalProcedures = RestOf<NewMedicalProcedures>;

export type PartialUpdateRestMedicalProcedures = RestOf<PartialUpdateMedicalProcedures>;

export type EntityResponseType = HttpResponse<IMedicalProcedures>;
export type EntityArrayResponseType = HttpResponse<IMedicalProcedures[]>;

@Injectable({ providedIn: 'root' })
export class MedicalProceduresService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medical-procedures');

  create(medicalProcedures: NewMedicalProcedures): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalProcedures);
    return this.http
      .post<RestMedicalProcedures>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(medicalProcedures: IMedicalProcedures): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalProcedures);
    return this.http
      .put<RestMedicalProcedures>(`${this.resourceUrl}/${this.getMedicalProceduresIdentifier(medicalProcedures)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(medicalProcedures: PartialUpdateMedicalProcedures): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalProcedures);
    return this.http
      .patch<RestMedicalProcedures>(`${this.resourceUrl}/${this.getMedicalProceduresIdentifier(medicalProcedures)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMedicalProcedures>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMedicalProcedures[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicalProceduresIdentifier(medicalProcedures: Pick<IMedicalProcedures, 'id'>): number {
    return medicalProcedures.id;
  }

  compareMedicalProcedures(o1: Pick<IMedicalProcedures, 'id'> | null, o2: Pick<IMedicalProcedures, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicalProceduresIdentifier(o1) === this.getMedicalProceduresIdentifier(o2) : o1 === o2;
  }

  addMedicalProceduresToCollectionIfMissing<Type extends Pick<IMedicalProcedures, 'id'>>(
    medicalProceduresCollection: Type[],
    ...medicalProceduresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicalProcedures: Type[] = medicalProceduresToCheck.filter(isPresent);
    if (medicalProcedures.length > 0) {
      const medicalProceduresCollectionIdentifiers = medicalProceduresCollection.map(medicalProceduresItem =>
        this.getMedicalProceduresIdentifier(medicalProceduresItem),
      );
      const medicalProceduresToAdd = medicalProcedures.filter(medicalProceduresItem => {
        const medicalProceduresIdentifier = this.getMedicalProceduresIdentifier(medicalProceduresItem);
        if (medicalProceduresCollectionIdentifiers.includes(medicalProceduresIdentifier)) {
          return false;
        }
        medicalProceduresCollectionIdentifiers.push(medicalProceduresIdentifier);
        return true;
      });
      return [...medicalProceduresToAdd, ...medicalProceduresCollection];
    }
    return medicalProceduresCollection;
  }

  protected convertDateFromClient<T extends IMedicalProcedures | NewMedicalProcedures | PartialUpdateMedicalProcedures>(
    medicalProcedures: T,
  ): RestOf<T> {
    return {
      ...medicalProcedures,
      dateProcedures: medicalProcedures.dateProcedures?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMedicalProcedures: RestMedicalProcedures): IMedicalProcedures {
    return {
      ...restMedicalProcedures,
      dateProcedures: restMedicalProcedures.dateProcedures ? dayjs(restMedicalProcedures.dateProcedures) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMedicalProcedures>): HttpResponse<IMedicalProcedures> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMedicalProcedures[]>): HttpResponse<IMedicalProcedures[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
