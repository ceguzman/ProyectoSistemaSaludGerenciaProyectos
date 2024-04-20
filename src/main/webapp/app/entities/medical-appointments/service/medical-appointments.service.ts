import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicalAppointments, NewMedicalAppointments } from '../medical-appointments.model';

export type PartialUpdateMedicalAppointments = Partial<IMedicalAppointments> & Pick<IMedicalAppointments, 'id'>;

type RestOf<T extends IMedicalAppointments | NewMedicalAppointments> = Omit<T, 'dateMedical'> & {
  dateMedical?: string | null;
};

export type RestMedicalAppointments = RestOf<IMedicalAppointments>;

export type NewRestMedicalAppointments = RestOf<NewMedicalAppointments>;

export type PartialUpdateRestMedicalAppointments = RestOf<PartialUpdateMedicalAppointments>;

export type EntityResponseType = HttpResponse<IMedicalAppointments>;
export type EntityArrayResponseType = HttpResponse<IMedicalAppointments[]>;

@Injectable({ providedIn: 'root' })
export class MedicalAppointmentsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medical-appointments');

  create(medicalAppointments: NewMedicalAppointments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalAppointments);
    return this.http
      .post<RestMedicalAppointments>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(medicalAppointments: IMedicalAppointments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalAppointments);
    return this.http
      .put<RestMedicalAppointments>(`${this.resourceUrl}/${this.getMedicalAppointmentsIdentifier(medicalAppointments)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(medicalAppointments: PartialUpdateMedicalAppointments): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalAppointments);
    return this.http
      .patch<RestMedicalAppointments>(`${this.resourceUrl}/${this.getMedicalAppointmentsIdentifier(medicalAppointments)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMedicalAppointments>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMedicalAppointments[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicalAppointmentsIdentifier(medicalAppointments: Pick<IMedicalAppointments, 'id'>): number {
    return medicalAppointments.id;
  }

  compareMedicalAppointments(o1: Pick<IMedicalAppointments, 'id'> | null, o2: Pick<IMedicalAppointments, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicalAppointmentsIdentifier(o1) === this.getMedicalAppointmentsIdentifier(o2) : o1 === o2;
  }

  addMedicalAppointmentsToCollectionIfMissing<Type extends Pick<IMedicalAppointments, 'id'>>(
    medicalAppointmentsCollection: Type[],
    ...medicalAppointmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicalAppointments: Type[] = medicalAppointmentsToCheck.filter(isPresent);
    if (medicalAppointments.length > 0) {
      const medicalAppointmentsCollectionIdentifiers = medicalAppointmentsCollection.map(medicalAppointmentsItem =>
        this.getMedicalAppointmentsIdentifier(medicalAppointmentsItem),
      );
      const medicalAppointmentsToAdd = medicalAppointments.filter(medicalAppointmentsItem => {
        const medicalAppointmentsIdentifier = this.getMedicalAppointmentsIdentifier(medicalAppointmentsItem);
        if (medicalAppointmentsCollectionIdentifiers.includes(medicalAppointmentsIdentifier)) {
          return false;
        }
        medicalAppointmentsCollectionIdentifiers.push(medicalAppointmentsIdentifier);
        return true;
      });
      return [...medicalAppointmentsToAdd, ...medicalAppointmentsCollection];
    }
    return medicalAppointmentsCollection;
  }

  protected convertDateFromClient<T extends IMedicalAppointments | NewMedicalAppointments | PartialUpdateMedicalAppointments>(
    medicalAppointments: T,
  ): RestOf<T> {
    return {
      ...medicalAppointments,
      dateMedical: medicalAppointments.dateMedical?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMedicalAppointments: RestMedicalAppointments): IMedicalAppointments {
    return {
      ...restMedicalAppointments,
      dateMedical: restMedicalAppointments.dateMedical ? dayjs(restMedicalAppointments.dateMedical) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMedicalAppointments>): HttpResponse<IMedicalAppointments> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMedicalAppointments[]>): HttpResponse<IMedicalAppointments[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
