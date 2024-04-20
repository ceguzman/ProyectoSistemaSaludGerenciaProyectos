import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeDiseases } from 'app/entities/type-diseases/type-diseases.model';
import { TypeDiseasesService } from 'app/entities/type-diseases/service/type-diseases.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { ClinicHistoryService } from '../service/clinic-history.service';
import { IClinicHistory } from '../clinic-history.model';
import { ClinicHistoryFormService, ClinicHistoryFormGroup } from './clinic-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-clinic-history-update',
  templateUrl: './clinic-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClinicHistoryUpdateComponent implements OnInit {
  isSaving = false;
  clinicHistory: IClinicHistory | null = null;

  typeDiseasesSharedCollection: ITypeDiseases[] = [];
  peopleSharedCollection: IPeople[] = [];

  protected clinicHistoryService = inject(ClinicHistoryService);
  protected clinicHistoryFormService = inject(ClinicHistoryFormService);
  protected typeDiseasesService = inject(TypeDiseasesService);
  protected peopleService = inject(PeopleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClinicHistoryFormGroup = this.clinicHistoryFormService.createClinicHistoryFormGroup();

  compareTypeDiseases = (o1: ITypeDiseases | null, o2: ITypeDiseases | null): boolean =>
    this.typeDiseasesService.compareTypeDiseases(o1, o2);

  comparePeople = (o1: IPeople | null, o2: IPeople | null): boolean => this.peopleService.comparePeople(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clinicHistory }) => {
      this.clinicHistory = clinicHistory;
      if (clinicHistory) {
        this.updateForm(clinicHistory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clinicHistory = this.clinicHistoryFormService.getClinicHistory(this.editForm);
    if (clinicHistory.id !== null) {
      this.subscribeToSaveResponse(this.clinicHistoryService.update(clinicHistory));
    } else {
      this.subscribeToSaveResponse(this.clinicHistoryService.create(clinicHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClinicHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(clinicHistory: IClinicHistory): void {
    this.clinicHistory = clinicHistory;
    this.clinicHistoryFormService.resetForm(this.editForm, clinicHistory);

    this.typeDiseasesSharedCollection = this.typeDiseasesService.addTypeDiseasesToCollectionIfMissing<ITypeDiseases>(
      this.typeDiseasesSharedCollection,
      clinicHistory.typeDisease,
    );
    this.peopleSharedCollection = this.peopleService.addPeopleToCollectionIfMissing<IPeople>(
      this.peopleSharedCollection,
      clinicHistory.people,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeDiseasesService
      .query()
      .pipe(map((res: HttpResponse<ITypeDiseases[]>) => res.body ?? []))
      .pipe(
        map((typeDiseases: ITypeDiseases[]) =>
          this.typeDiseasesService.addTypeDiseasesToCollectionIfMissing<ITypeDiseases>(typeDiseases, this.clinicHistory?.typeDisease),
        ),
      )
      .subscribe((typeDiseases: ITypeDiseases[]) => (this.typeDiseasesSharedCollection = typeDiseases));

    this.peopleService
      .query()
      .pipe(map((res: HttpResponse<IPeople[]>) => res.body ?? []))
      .pipe(map((people: IPeople[]) => this.peopleService.addPeopleToCollectionIfMissing<IPeople>(people, this.clinicHistory?.people)))
      .subscribe((people: IPeople[]) => (this.peopleSharedCollection = people));
  }
}
