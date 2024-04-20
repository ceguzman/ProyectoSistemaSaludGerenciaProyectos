import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeDocumentDetailComponent } from './type-document-detail.component';

describe('TypeDocument Management Detail Component', () => {
  let comp: TypeDocumentDetailComponent;
  let fixture: ComponentFixture<TypeDocumentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeDocumentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeDocumentDetailComponent,
              resolve: { typeDocument: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeDocumentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typeDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeDocumentDetailComponent);

      // THEN
      expect(instance.typeDocument()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
