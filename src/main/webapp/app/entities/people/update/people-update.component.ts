import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeDocument } from 'app/entities/type-document/type-document.model';
import { TypeDocumentService } from 'app/entities/type-document/service/type-document.service';
import { ITypeSpecialist } from 'app/entities/type-specialist/type-specialist.model';
import { TypeSpecialistService } from 'app/entities/type-specialist/service/type-specialist.service';
import { PeopleService } from '../service/people.service';
import { IPeople } from '../people.model';
import { PeopleFormService, PeopleFormGroup } from './people-form.service';

@Component({
  standalone: true,
  selector: 'jhi-people-update',
  templateUrl: './people-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PeopleUpdateComponent implements OnInit {
  isSaving = false;
  people: IPeople | null = null;

  typeDocumentsSharedCollection: ITypeDocument[] = [];
  typeSpecialistsSharedCollection: ITypeSpecialist[] = [];

  protected peopleService = inject(PeopleService);
  protected peopleFormService = inject(PeopleFormService);
  protected typeDocumentService = inject(TypeDocumentService);
  protected typeSpecialistService = inject(TypeSpecialistService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PeopleFormGroup = this.peopleFormService.createPeopleFormGroup();

  compareTypeDocument = (o1: ITypeDocument | null, o2: ITypeDocument | null): boolean =>
    this.typeDocumentService.compareTypeDocument(o1, o2);

  compareTypeSpecialist = (o1: ITypeSpecialist | null, o2: ITypeSpecialist | null): boolean =>
    this.typeSpecialistService.compareTypeSpecialist(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ people }) => {
      this.people = people;
      if (people) {
        this.updateForm(people);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const people = this.peopleFormService.getPeople(this.editForm);
    if (people.id !== null) {
      this.subscribeToSaveResponse(this.peopleService.update(people));
    } else {
      this.subscribeToSaveResponse(this.peopleService.create(people));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeople>>): void {
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

  protected updateForm(people: IPeople): void {
    this.people = people;
    this.peopleFormService.resetForm(this.editForm, people);

    this.typeDocumentsSharedCollection = this.typeDocumentService.addTypeDocumentToCollectionIfMissing<ITypeDocument>(
      this.typeDocumentsSharedCollection,
      people.typeDocument,
    );
    this.typeSpecialistsSharedCollection = this.typeSpecialistService.addTypeSpecialistToCollectionIfMissing<ITypeSpecialist>(
      this.typeSpecialistsSharedCollection,
      people.typeSpecialist,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeDocumentService
      .query()
      .pipe(map((res: HttpResponse<ITypeDocument[]>) => res.body ?? []))
      .pipe(
        map((typeDocuments: ITypeDocument[]) =>
          this.typeDocumentService.addTypeDocumentToCollectionIfMissing<ITypeDocument>(typeDocuments, this.people?.typeDocument),
        ),
      )
      .subscribe((typeDocuments: ITypeDocument[]) => (this.typeDocumentsSharedCollection = typeDocuments));

    this.typeSpecialistService
      .query()
      .pipe(map((res: HttpResponse<ITypeSpecialist[]>) => res.body ?? []))
      .pipe(
        map((typeSpecialists: ITypeSpecialist[]) =>
          this.typeSpecialistService.addTypeSpecialistToCollectionIfMissing<ITypeSpecialist>(typeSpecialists, this.people?.typeSpecialist),
        ),
      )
      .subscribe((typeSpecialists: ITypeSpecialist[]) => (this.typeSpecialistsSharedCollection = typeSpecialists));
  }
}
